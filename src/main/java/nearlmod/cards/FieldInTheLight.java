package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.ChooseSpecificCardAction;
import nearlmod.characters.Nearl;
import nearlmod.patches.AbstractCardEnum;

import java.util.ArrayList;
import java.util.Collections;

public class FieldInTheLight extends AbstractNearlCard {
    public static final String ID = "nearlmod:FieldInTheLight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/fieldinthelight.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public FieldInTheLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);

        previewList = Nearl.getUnuniqueFriendCard(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> list = Nearl.getUnuniqueFriendCard(true);
        Collections.shuffle(list);
        addToBot(new ChooseSpecificCardAction(new ArrayList<>(list.subList(0, 3)), false));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FieldInTheLight();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
