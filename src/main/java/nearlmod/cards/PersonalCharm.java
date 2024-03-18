package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonFriendAction;
import nearlmod.characters.Nearl;
import nearlmod.orbs.*;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

import java.util.ArrayList;
import java.util.Collections;

public class PersonalCharm extends AbstractNearlCard {
    public static final String ID = "nearlmod:PersonalCharm";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/personalcharm.png";
    private static final int COST = 2;

    public PersonalCharm() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);

        previewList = Nearl.getUnuniqueFriendCard(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractFriend> list = new ArrayList<>();
        list.add(new Flametail());
        list.add(new Wildmane());
        list.add(new JusticeKnight());
        list.add(new Fartooth());
        list.add(new Ashlock());
        Collections.shuffle(list);
        addToBot(new SummonFriendAction(list.get(0)));
        if (upgraded && list.size() >= 2)
            addToBot(new SummonFriendAction(list.get(1)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PersonalCharm();
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
