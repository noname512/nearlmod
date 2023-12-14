package nearlmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import nearlmod.cards.friendcards.AbstractFriendCard;

public class BFPPower extends AbstractPower implements CloneablePowerInterface {
    public static final String POWER_ID = "nearlmod:BFPPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public int cardPlayed;

    public BFPPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/bfp power 128.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("images/powers/bfp power 48.png"), 0, 0, 48, 48);
        type = PowerType.BUFF;
        cardPlayed = 0;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BFPPower(owner);
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (cardPlayed != 1) return;
        if (info.type != DamageInfo.DamageType.NORMAL) return;
        if (info.name != null && info.name.endsWith(AbstractFriendCard.damageSuffix)) return;
        int effectType = AbstractDungeon.cardRandomRng.random(1, 2);
        if (owner.hasPower("nearlmod:WFPPower")) effectType = 3;
        if ((effectType & 1) != 0)
            addToBot(new ApplyPowerAction(target, info.owner, new WeakPower(target, 1, false)));
        if ((effectType & 2) != 0)
            addToBot(new ApplyPowerAction(target, info.owner, new VulnerablePower(target, 1, false)));
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof AbstractFriendCard || card.type != AbstractCard.CardType.ATTACK) return;
        cardPlayed++;
    }

    @Override
    public void atStartOfTurn() {
        cardPlayed = 0;
    }
}
