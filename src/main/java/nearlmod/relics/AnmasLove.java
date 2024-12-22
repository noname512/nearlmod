package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.unique.FeedAction;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.chests.BossChest;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.rooms.TrueVictoryRoom;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.actions.WeakenAllAction;
import nearlmod.orbs.Aurora;
import nearlmod.patches.CurseRelicPatch;

public class AnmasLove extends CustomRelic {

    public static final String ID = "nearlmod:AnmasLove";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/revenge.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/revenge_p.png");
    public int triggerIf = 2;       // (triggerIf + 1) percent
    public AnmasLove() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            addToBot(new GainEnergyAction(1));
        }
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            addToBot(new PureDamageAllEnemiesAction(AbstractDungeon.player, 10, "Anma", AbstractGameAction.AttackEffect.NONE, DamageInfo.DamageType.THORNS));
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (counter != -1) {
            AbstractDungeon.ascensionLevel = counter;
            counter = -1;
        }
        if ((AbstractDungeon.getCurrRoom() instanceof VictoryRoom) || (AbstractDungeon.getCurrRoom() instanceof TrueVictoryRoom) || (AbstractDungeon.getCurrRoom() instanceof TreasureRoomBoss)) {
            return;
            //Cannot trigger this!
        }
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            this.counter = AbstractDungeon.ascensionLevel;
            AbstractDungeon.ascensionLevel = 0;
        }
    }

    @Override
    public int onPlayerHeal(int healAmount) {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            return healAmount * 2;
        }
        else {
            return healAmount;
        }
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        if ((damageAmount > 0) && (AbstractDungeon.relicRng.random(0, 99) <= triggerIf)) {
            this.flash();
            return 0;
        }
        else {
            return damageAmount;
        }
    }

    @Override
    public void atPreBattle() {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            addToBot(new SummonFriendAction(new Aurora()));
        }
    }

    @Override
    public void onVictory() {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            AbstractDungeon.getCurrRoom().addCardToRewards();
        }
    }

    @Override
    public void onChestOpen(boolean bossChest) {
        if (!bossChest && AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(AbstractDungeon.returnRandomPotion()));;
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            AbstractDungeon.player.increaseMaxHp(3, true);
        }
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        if (!c.upgraded && c.canUpgrade() && AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            c.upgrade();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AnmasLove();
    }
}
