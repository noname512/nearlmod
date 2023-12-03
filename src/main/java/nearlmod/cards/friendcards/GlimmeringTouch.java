package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.UseShadowAction;
import nearlmod.orbs.Viviana;
import nearlmod.powers.GlimmeringTouchPower;
import nearlmod.patches.AbstractCardEnum;

public class GlimmeringTouch extends AbstractFriendCard {
    public static final String ID = "nearlmod:GlimmeringTouch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/glimmeringtouch.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 40;
    private static final int LIGHT_GAIN = 10;

    public GlimmeringTouch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Viviana");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = LIGHT_GAIN;
        isSecondMagicNumberUseTrust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof Viviana)
                ((Viviana)orb).startCharging(upgraded? 1 : 2);
        addToBot(new ApplyPowerAction(p, p, new GlimmeringTouchPower(p, upgraded? 1 : 2, magicNumber, secondMagicNumber)));
        addToBot(new UseShadowAction(p));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p.getPower("nearlmod:Shadow") != null) {
            magicNumber += p.getPower("nearlmod:Shadow").amount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (p.hasPower("nearlmod:GlimmeringTouchPower")) {
            this.cantUseMessage = "薇薇安娜正在蓄力中";
            return false;
        }
        return true;
    }
    @Override
    public AbstractCard makeCopy() {
        return new GlimmeringTouch();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
