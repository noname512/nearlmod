package nearlmod.cards;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.KnightCompetitionAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class KnightCompetition extends AbstractNearlCard {
    public static final String ID = "nearlmod:KnightCompetition";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/knightcompetition.png";
    private static final int COST = 1;
    private static final int COIN_AMT = 15;
    private static final int UPGRADE_PLUS_COIN = 5;

    public KnightCompetition() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        magicNumber = baseMagicNumber = COIN_AMT;
        tags.add(NearlTags.IS_USE_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(DefStance.STANCE_ID))
            addToBot(new ChangeStanceAction(new AtkStance()));
        addToBot(new KnightCompetitionAction(magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new KnightCompetition();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_COIN);
        }
    }
}
