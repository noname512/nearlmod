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
    private static final int CHARGE_TURN = 2;
    private static final int UPGRADE_CHARGE_TURN = 1;
    private static final int ATTACK_DMG = 30;

    public GlimmeringTouch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Viviana");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = CHARGE_TURN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof Viviana)
                ((Viviana)orb).startCharging(secondMagicNumber);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GlimmeringTouchPower(p, secondMagicNumber, magicNumber)));
        AbstractDungeon.actionManager.addToBottom(new UseShadowAction(p));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p.getPower("nearlmod:Shadow") != null)
        {
            magicNumber += p.getPower("nearlmod:Shadow").amount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
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
            secondMagicNumber = UPGRADE_CHARGE_TURN;
            initializeDescription();
        }
    }
}
