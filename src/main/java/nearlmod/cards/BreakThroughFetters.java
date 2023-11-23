package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.BreakThroughFettersAction;
import nearlmod.patches.AbstractCardEnum;

public class BreakThroughFetters extends AbstractNearlCard {
    public static final String ID = "nearlmod:BreakThroughFetters";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = 1;
    private static final int LIGHT_COST = 4;
    private static final int UPGRADE_PLUS_COST = -1;

    public BreakThroughFetters() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = LIGHT_COST;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BreakThroughFettersAction(getLightAmount() / magicNumber, magicNumber));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (getLightAmount() < magicNumber) return false;
        for (AbstractCard c : p.drawPile.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                return true;
        for (AbstractCard c : p.discardPile.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                return true;
        for (AbstractCard c : p.hand.group)
            if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                return true;
        return false;
    }

    private int getLightAmount() {
        if (!AbstractDungeon.player.hasPower("nearlmod:LightPower")) return 0;
        else return AbstractDungeon.player.getPower("nearlmod:LightPower").amount;
    }

    @Override
    public AbstractCard makeCopy() {
        return new BreakThroughFetters();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_COST);
        }
    }
}
