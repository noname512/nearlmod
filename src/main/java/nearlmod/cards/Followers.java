package nearlmod.cards;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.ChooseSpecificCardAction;
import nearlmod.cards.special.NearlCard;
import nearlmod.cards.special.NightingaleCard;
import nearlmod.cards.special.ShiningCard;
import nearlmod.orbs.Nightingale;
import nearlmod.orbs.Shining;
import nearlmod.patches.AbstractCardEnum;

import java.util.ArrayList;

public class Followers extends AbstractNearlCard {
    public static final String ID = "nearlmod:Followers";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/followers.png";
    private static final int COST = 1;
    public Followers() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        previewList = new ArrayList<>();
        previewList.add(new NearlCard());
        previewList.add(new NightingaleCard());
        previewList.add(new ShiningCard());
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Shining.ORB_ID) && NLMOD.checkOrb(Nightingale.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            for (AbstractCard card : previewList) addToBot(new MakeTempCardInHandAction(card));
        } else {
            addToBot(new ChooseSpecificCardAction(previewList, false));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Followers();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            selfRetain = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
