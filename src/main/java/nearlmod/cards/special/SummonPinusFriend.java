package nearlmod.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonOrbAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.*;

public class SummonPinusFriend extends AbstractNearlCard {
    public static final String ID = "nearlmod:SummonPinusFriend";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private final String friendID;

    public SummonPinusFriend(String friendID) {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.NONE);
        this.friendID = friendID;
        OrbStrings orbStrings = CardCrawlGame.languagePack.getOrbString(friendID);
        name = orbStrings.NAME;
        rawDescription = EXTENDED_DESCRIPTION[0] + name + EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        AbstractFriend o;
        switch (friendID) {
            case Flametail.ORB_ID:
                o = new Flametail();
                break;
            case Fartooth.ORB_ID:
                o = new Fartooth();
                break;
            case Wildmane.ORB_ID:
                o = new Wildmane();
                break;
            case Ashlock.ORB_ID:
                o = new Ashlock();
                break;
            case JusticeKnight.ORB_ID:
                o = new JusticeKnight();
                break;
            default:
                return;
        }
        addToBot(new SummonOrbAction(o));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SummonPinusFriend(friendID);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {}
}
