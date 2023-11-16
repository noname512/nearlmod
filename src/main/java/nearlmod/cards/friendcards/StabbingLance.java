package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.MyRandomAttackAction;
import nearlmod.actions.StabbingLanceAction;
import nearlmod.patches.AbstractCardEnum;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class StabbingLance extends AbstractFriendCard {
    public static final String ID = "nearlmod:StabbingLance";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flameheart.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 2;
    private static final int ATTACK_TIMES = 6;
    private static final int UPGRADE_PLUS_TIMES = 2;

    public StabbingLance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Wildmane");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_TIMES;
        tags.add(IS_KNIGHT_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int resMonster = 0;
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (!ms.isDeadOrEscaped())
                resMonster++;
        for (int i = 1; i <= secondMagicNumber; i++)
            addToBot(new MyRandomAttackAction(p, magicNumber));
        addToBot(new StabbingLanceAction(resMonster, upgraded? 2 : 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new StabbingLance();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_TIMES);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
