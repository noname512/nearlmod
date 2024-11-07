package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.actions.ChooseSpecificCardAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.cards.friendcards.FlashFade;
import nearlmod.cards.friendcards.GlimmeringTouch;
import nearlmod.cards.friendcards.LSSwiftSword;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

import java.util.ArrayList;

public class SightImpairingShowdown extends AbstractNearlCard {
    public static final String ID = "nearlmod:SightImpairingShowdown";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/sightimpairingshowdown.png";
    private static final int COST = 0;
    private static final int POWER_GAIN = 1;

    public SightImpairingShowdown() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = POWER_GAIN;
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Viviana.ORB_ID;

        previewList = new ArrayList<>();
        previewList.add(new LSSwiftSword());
        previewList.add(new GlimmeringTouch());
        previewList.add(new FlashFade());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof Viviana)
                ((Viviana)orb).applyStrength(magicNumber);
        if (!upgraded) {
            AbstractFriendCard card = Viviana.getRandomCard(false, true);
            addToBot(new AddFriendCardToHandAction(card));
        } else {
            addToBot(new ChooseSpecificCardAction(previewList));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SightImpairingShowdown();
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
