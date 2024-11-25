package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import nearlmod.actions.EndBattleAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.special.IceCone;
import nearlmod.orbs.Aurora;
import nearlmod.powers.ShelteredEnemy;

public class TheWillOfSami extends AbstractMonster {
    public static final String ID = "nearlmod:TheWillOfSami";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public final int BattleEndTurn;
    public int turn;
    public boolean isStage2;
    public final int IceNum;
    public TheWillOfSami(float x, float y) {
        super(NAME, ID, 200, 10.0F, 0, 570.0F, 520.0F, null, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9)
            setHp(210);
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 15));
            this.damage.add(new DamageInfo(this, 40));
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 9));
            this.damage.add(new DamageInfo(this, 13));
            this.damage.add(new DamageInfo(this, 33));
        } else {
            this.damage.add(new DamageInfo(this, 8));
            this.damage.add(new DamageInfo(this, 12));
            this.damage.add(new DamageInfo(this, 25));
        }
        turn = 1;
        isStage2 = false;
        if (AbstractDungeon.ascensionLevel < 15) {
            BattleEndTurn = 20;
            IceNum = 1;
        } else {
            BattleEndTurn = 16;
            IceNum = 2;
        }
        loadAnimation("resources/nearlmod/images/monsters/enemy_2054_smdeer/enemy_2054_smdeer33.atlas", "resources/nearlmod/images/monsters/enemy_2054_smdeer/enemy_2054_smdeer33.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new ShelteredEnemy(this)));
        addToBot(new SummonFriendAction(new Aurora()));
        addToBot(new SummonFriendAction(new Aurora()));
    }

    void gameOver() {
        AbstractDungeon.actionManager.cleanCardQueue();
        AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
        AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.getCurrRoom().smoked = true;
        addToBot(new EndBattleAction());
    }

    @Override
    public void takeTurn() {
        if (this.nextMove <= 2) {
            state.setAnimation(0, "Attack", false);
            state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            if (isStage2) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            }
            if (this.nextMove == 2) {
                addToBot(new MakeTempCardInHandAction(new IceCone(), IceNum));
                if (isStage2) {
                    addToBot(new MakeTempCardInHandAction(new IceCone(), IceNum));
                }
            }
        } else {
            state.setAnimation(0, "Skill_02", false);
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(2)));
            gameOver();
        }
        turn ++;
        rollMove();
    }

    @Override
    public void rollMove() {
        if (currentHealth * 2 <= maxHealth) {
            isStage2 = true;
        }
        if (turn >= BattleEndTurn) {
            setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(2).base);
        } else {
            if (turn % 4 == 0) {
                if (isStage2) {
                    setMove((byte) 2, Intent.ATTACK_DEBUFF,  this.damage.get(1).base, 2, true);
                } else {
                    setMove((byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                }
            } else {
                if (isStage2) {
                    setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base,2,true);
                } else {
                    setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
                }
            }
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
    }
}
