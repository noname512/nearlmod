package nearlmod.cards.friendcards;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.actions.GainCostAction;
import nearlmod.characters.Nearl;
import nearlmod.patches.AbstractCardEnum;

import java.util.ArrayList;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class PinusSylvestris extends AbstractFriendCard {
    public static final String ID = "nearlmod:PinusSylvestris";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/pinussylvestris.png";
    private static final int COST = 1;
    private static final int COST_GAIN = 2;
    private static final int UPGRADE_PLUS_COST = 1;

    boolean hasPreview;

    public PinusSylvestris() {
        this(true);
    }

    public PinusSylvestris(boolean hasPreview) {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Flametail");
        secondMagicNumber = baseSecondMagicNumber = COST_GAIN;
        bannerSmallRegion = ImageMaster.CARD_BANNER_UNCOMMON;
        bannerLargeRegion = ImageMaster.CARD_BANNER_UNCOMMON_L;
        tags.add(IS_KNIGHT_CARD);

        this.hasPreview = hasPreview;
        if (hasPreview) {
            previewList = Nearl.getUnuniqueFriendCard(true);
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (!hasPreview) {
            hasPreview = true;
            previewList = Nearl.getUnuniqueFriendCard(true);
            if (upgraded) {
                for (AbstractCard x: previewList) {
                    x.upgrade();
                }
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainCostAction(secondMagicNumber));
        ArrayList<AbstractCard> list = Nearl.getUnuniqueFriendCard(true);
        int random = AbstractDungeon.cardRng.random(0, list.size() - 1);
        AbstractFriendCard card = (AbstractFriendCard) list.get(random);
        if (upgraded) card.upgrade();
        addToBot(new AddFriendCardToHandAction(card));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PinusSylvestris(hasPreview);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            if (hasPreview) {
                for (AbstractCard x: previewList) {
                    x.upgrade();
                }
            }
            upgradeSecondMagicNumber(UPGRADE_PLUS_COST);
        }
    }
}
