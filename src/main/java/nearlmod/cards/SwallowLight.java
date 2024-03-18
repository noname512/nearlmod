package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.actions.SummonFriendAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.relics.NormalPerson;

public class SwallowLight extends AbstractNearlCard {
    public static final String ID = "nearlmod:SwallowLight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/swallowlight.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    public SwallowLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);

        previewList = Nearl.getFriendCard(Viviana.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower power = p.getPower("nearlmod:LightPower");
        int amount = 0;
        if (power != null) {
            amount = power.amount;
            addToBot(new RemoveSpecificPowerAction(p, p, power));
            if (!upgraded) amount = (amount + 2) / 3;
            else amount = (amount + 1) / 2;
        }
        if (p.hasRelic(NormalPerson.ID)) {
            p.getRelic(NormalPerson.ID).flash();
            return;
        }
        addToBot(new SummonFriendAction(new Viviana(amount)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwallowLight();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
