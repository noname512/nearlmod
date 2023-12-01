package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.*;
import nearlmod.patches.NearlTags;

import java.util.ArrayList;

public class PersonalCharmSp extends AbstractNearlCard {
    public static final String ID = "nearlmod:PersonalCharmSp";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/personalcharm.png";
    private static final int COST = 2;

    public PersonalCharmSp() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.RARE, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choice = new ArrayList<>();
        choice.add(new SummonPinusFriend(Flametail.ORB_ID));
        choice.add(new SummonPinusFriend(Ashlock.ORB_ID));
        choice.add(new SummonPinusFriend(Fartooth.ORB_ID));
        choice.add(new SummonPinusFriend(Wildmane.ORB_ID));
        choice.add(new SummonPinusFriend(JusticeKnight.ORB_ID));
        addToBot(new ChooseOneAction(choice));
        if (upgraded)
            addToBot(new ChooseOneAction(choice));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PersonalCharmSp();
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
