package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AllinOne;
import nearlmod.cards.special.ToAtkStance;
import nearlmod.cards.special.ToDefStance;
import nearlmod.patches.AbstractCardEnum;

import java.util.ArrayList;

public class CraftsmanEcho extends AbstractFriendCard {
    public static final String ID = "nearlmod:CraftsmanEcho";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/craftsmanecho.png";
    private static final int COST = 2;
    private static final int EXTRA_INC = 3;
    private static final int UPGRADE_PLUS_COST = 1;

    public CraftsmanEcho() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Blemishine");
        secondMagicNumber = baseSecondMagicNumber = EXTRA_INC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choice = new ArrayList<>();
        choice.add(new ToDefStance(secondMagicNumber));
        choice.add(new ToAtkStance(secondMagicNumber));
        addToBot(new ChooseOneAction(choice));
        AbstractCard card = new AllinOne();
        card.upgrade();
        card.exhaust = true;
        card.rawDescription += EXTENDED_DESCRIPTION[0];
        card.initializeDescription();
        addToBot(new MakeTempCardInHandAction(card, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CraftsmanEcho();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_PLUS_COST);
        }
    }
}
