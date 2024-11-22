package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.NLMOD;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;
import nearlmod.orbs.*;
import nearlmod.patches.AbstractCardEnum;

public class UnitedAsOne extends AbstractNearlCard {
    public static final String ID = "nearlmod:UnitedAsOne";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/unitedasone.png";
    private static final int COST = 0;

    public UnitedAsOne() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof AbstractFriend) {
                if (!((AbstractFriend) orb).uniqueUsed) {
                    AbstractFriendCard c = ((AbstractFriend) orb).getUniqueCard();
                    if (c != null) {
                        addToBot(new AddFriendCardToHandAction(c, upgraded));
                    }
                }
            }
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnitedAsOne();
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
