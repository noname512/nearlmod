package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonOrbAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.orbs.Nightingale;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class DanceTogether extends AbstractNearlCard {
    public static final String ID = "nearlmod:DanceTogether";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/dancetogether.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;

    public DanceTogether() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonOrbAction(new Nightingale()));
        AbstractFriendCard card = Nightingale.getRandomCard(false, true);
        if (upgraded) {
            card.rawDescription = card.baseDescription + " NL 保留 。 NL 消耗 。";
            card.isEthereal = false;
            card.selfRetain = true;
            card.initializeDescription();
        }
        AbstractDungeon.player.hand.addToHand(card);
    }

    @Override
    public AbstractCard makeCopy() {
        return new DanceTogether();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
