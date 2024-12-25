package nearlmod.monsters;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import nearlmod.actions.EndBattleAction;
import nearlmod.actions.RemoveLastFriendAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.special.BlockCard;
import nearlmod.characters.Nearl;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Penance;
import nearlmod.powers.HiddenPower;
import nearlmod.powers.HintPower;
import nearlmod.relics.OldRules;

public class FamigliaCleaner extends AbstractMonster {
    public static final String ID = "nearlmod:FamigliaCleaner";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    public FamigliaCleaner(float x, float y) {
        super(NAME, ID, 60, 10.0F, 0, 170.0F, 320.0F, null, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8)
            setHp(65);
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 16));
            this.damage.add(new DamageInfo(this, 22));
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 15));
            this.damage.add(new DamageInfo(this, 21));
        } else {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 14));
            this.damage.add(new DamageInfo(this, 20));
        }
        loadAnimation("resources/nearlmod/images/monsters/enemy_1283_sgkill_2/enemy_1283_sgkill_233.atlas", "resources/nearlmod/images/monsters/enemy_1283_sgkill_2/enemy_1283_sgkill_233.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new HintPower(this, 1)));
        addToBot(new ApplyPowerAction(this, this, new HiddenPower(this)));
        addToBot(new SummonFriendAction(new Penance()));
        if (AbstractDungeon.ascensionLevel < 15)
            addToBot(new MakeTempCardInHandAction(new BlockCard()));
        addToBot(new TalkAction(AbstractDungeon.player, DIALOG[0], 0.3F, 3.0F));
    }

    public void blocked()
    {
        setMove((byte) 3, Intent.ATTACK, this.damage.get(2).base);
        createIntent();
        addToBot(new RemoveSpecificPowerAction(this, this, HiddenPower.POWER_ID));
    }

    void attackFriend(DamageInfo info) {
        int def_val = AbstractDungeon.player.currentBlock + TempHPField.tempHp.get(AbstractDungeon.player);
        if (info.output > def_val) {
            if (def_val > 0) {
                addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, def_val)));
            }
            AbstractFriend friend = ((Nearl)AbstractDungeon.player).lastFriend();
            if (friend != null) {
                addToTop(new RemoveLastFriendAction());
                addToTop(new WaitAction(1.3F));
            }
            if (friend instanceof Penance) {
                gameOver();
            }
        } else {
            addToTop(new DamageAction(AbstractDungeon.player, info));
            addToTop(new WaitAction(0.5F));
        }
    }

    void gameOver() {
        AbstractDungeon.actionManager.cleanCardQueue();
        AbstractDungeon.effectList.add(new DeckPoofEffect(64.0F * Settings.scale, 64.0F * Settings.scale, true));
        AbstractDungeon.effectList.add(new DeckPoofEffect((float)Settings.WIDTH - 64.0F * Settings.scale, 64.0F * Settings.scale, false));
        AbstractDungeon.overlayMenu.hideCombatPanels();
        AbstractDungeon.getCurrRoom().smoked = true;
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new OldRules());
        addToBot(new EndBattleAction());
    }

    @Override
    public void takeTurn() {
        if (nextMove <= 2) {
            state.setAnimation(0, "Attack", false);
            state.addAnimation(0, "Idle", true, 0);
            attackFriend(damage.get(nextMove - 1));
        } else {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(2)));
        }
        setMove((byte) 2, Intent.ATTACK_DEBUFF, damage.get(1).base);

        addToBot(new MakeTempCardInHandAction(new BlockCard()));
        if (!hasPower(HiddenPower.POWER_ID)) {
            addToBot(new ApplyPowerAction(this, this ,new HiddenPower(this)));
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }
}
