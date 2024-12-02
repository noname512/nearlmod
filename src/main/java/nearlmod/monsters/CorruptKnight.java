package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.Panacea;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.NightScouringGleam;
import nearlmod.orbs.Blemishine;
import nearlmod.patches.CharacterSettingPatch;
import nearlmod.powers.DoubleBossPower;
import nearlmod.powers.FriendShelterPower;

public class CorruptKnight extends AbstractMonster {
    public static final String ID = "nearlmod:CorruptKnight";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "resources/nearlmod/images/monsters/corruptknight.png";
    public boolean isWhitheredDead = false;
    private int currentTurn;

    public CorruptKnight(float x, float y) {
        super(NAME, ID, 130, 25.0F, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9)
            setHp(145);
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.damage.add(new DamageInfo(this, 20));
            this.damage.add(new DamageInfo(this, 99));
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 20));
            this.damage.add(new DamageInfo(this, 90));
        } else {
            this.damage.add(new DamageInfo(this, 18));
            this.damage.add(new DamageInfo(this, 90));
        }
        loadAnimation("resources/nearlmod/images/monsters/enemy_1513_dekght/enemy_1513_dekght.atlas", "resources/nearlmod/images/monsters/enemy_1513_dekght/enemy_1513_dekght37.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new DoubleBossPower(this)));
        currentTurn = 0;
    }

    @Override
    public void takeTurn() {
        currentTurn++;
        AbstractPlayer p = AbstractDungeon.player;
        if (currentTurn == 2) {
            for (AbstractOrb orb : p.orbs)
                if (orb instanceof Blemishine) {
                    AbstractDungeon.effectList.add(new SpeechBubble(orb.hb.cX + 60.0F, orb.hb.cY + 30.0F, 3.0F, DIALOG[3], true));
                    break;
                }
            addToTop(new ApplyPowerAction(p, p, new FriendShelterPower(p)));
        }
        int attTimes = 1;
        if (isWhitheredDead) attTimes++;
        if (this.nextMove == 2) {
            addToBot(new WaitAction(2.5F));
            this.state.setAnimation(0, "Skill_End", false);
            this.state.addAnimation(0, "Idle", true, 0);
            for (int i = 0; i < attTimes; i++) {
                addToBot(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            }
            setMove((byte) 3, Intent.ATTACK, this.damage.get(0).base, attTimes, (attTimes > 1));
        } else {
            addToBot(new WaitAction(1.0F));
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0);
            for (int i = 0; i < attTimes; i++) {
                addToBot(new DamageAction(p, this.damage.get(0)));
            }
            if ((this.nextMove != 7) && (this.nextMove != 1)) {
                setMove((byte) (this.nextMove + 1), Intent.ATTACK, this.damage.get(0).base, attTimes, (attTimes > 1));
            } else {
                setMove(MOVES[0], (byte) 2, Intent.ATTACK, this.damage.get(1).base, attTimes, (attTimes > 1));
                this.state.addAnimation(0, "Skill_Start", false, 10.0F);
                this.state.addAnimation(0, "Skill_Loop", true, 0);
            }
        }
        if (this.nextMove == 1) {
            addToBot(new TalkAction(this, DIALOG[0], 2.5F, 3.0F));
            AbstractMonster ms = AbstractDungeon.getMonsters().getMonster("nearlmod:WitheredKnight");
            if (ms != null)
                addToBot(new TalkAction(ms, DIALOG[1], 0.3F, 3.0F));
        }
    }

    @Override
    protected void getMove(int i) {
        if ((AbstractDungeon.ascensionLevel >= 15) && (CharacterSettingPatch.curTeam == 1)) {
            setMove((byte) 3, Intent.ATTACK, this.damage.get(0).base);
        }
        else {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
    }

    public void die() {
        super.die();
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (ms instanceof WitheredKnight && !ms.isDeadOrEscaped()) {
                ms.getPower("nearlmod:DoubleBoss").onSpecificTrigger();
                ((WitheredKnight) ms).CorruptedDead();
            }
    }

    public void WhitheredDead() {
        isWhitheredDead = true;
        addToBot(new TalkAction(this, DIALOG[2], 0.3F, 3.0F));
        if (this.nextMove == 2) {
            setMove(MOVES[0], (byte) 2, Intent.ATTACK, this.damage.get(1).base, 2, true);
            this.createIntent();
        }
        else {
            setMove(this.nextMove, Intent.ATTACK, this.damage.get(0).base, 2, true);
            this.createIntent();
        }
    }
}
