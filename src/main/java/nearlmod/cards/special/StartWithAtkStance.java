package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.stances.AtkStance;

public class StartWithAtkStance extends AbstractNearlCard {
    public static final String ID = "nearlmod:StartWithAtkStance";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = "images/cards/toatk.png";

    public StartWithAtkStance() {
        super(ID, NAME, IMG_PATH, -2, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.NONE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        AtkStance.atkInc = -1;
        addToBot(new ChangeStanceAction(new AtkStance()));
    }

    @Override
    public void upgrade() {}

    @Override
    public AbstractCard makeCopy() {
        return new StartWithAtkStance();
    }
}
