package nearlmod.cards;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class FlutteringWings extends AbstractNearlCard {
    public static final String ID = "nearlmod:FlutteringWings";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flutteringwings.png";
    private static final int COST = 0;

    public FlutteringWings() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        cardsToPreview = new AllinOne();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AtkStance.keepVal = true;
            addToBot(new ChangeStanceAction(new DefStance()));
        } else {
            DefStance.keepVal = true;
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
        AbstractCard c = new AllinOne();
        c.isEthereal = true;
        c.exhaust = true;
        if (upgraded || p.hasPower("MasterRealityPower"))
            c.upgrade();
        p.hand.addToHand(c);
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlutteringWings();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
