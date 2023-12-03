package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
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
    public static final String IMG_PATH = "images/cards/autoprotect.png";
    private static final int COST = -2;
    private static final int BLOCK_DMT = 2;
    private static final int UPGRADE_PLUS_AMT = 1;

    public AutoProtect() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Shining");
        magicNumber = baseMagicNumber = BLOCK_DMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void triggerWhenCopied() {
        AbstractPlayer p = AbstractDungeon.player;
        addToTop(new GainBlockAction(p, magicNumber));
        addToTop(new ApplyPowerAction(p, p, new NextTurnBlockPower(p, magicNumber, NAME)));
        addToTop(new ExhaustSpecificCardAction(this, p.hand));
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
