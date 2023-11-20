package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import nearlmod.patches.AbstractCardEnum;

public class AutoProtect extends AbstractFriendCard {
    public static final String ID = "nearlmod:AutoProtect";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/lsswiftsword.png";
    private static final int COST = 1;
    private static final int BLOCK_DMT = 5;
    private static final int UPGRADE_PLUS_AMT = 3;

    public AutoProtect() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Shining");
        magicNumber = baseMagicNumber = BLOCK_DMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, magicNumber));
        addToBot(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, magicNumber, NAME)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AutoProtect();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_AMT);
        }
    }
}
