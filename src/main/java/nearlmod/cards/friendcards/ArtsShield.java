package nearlmod.cards.friendcards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;

public class ArtsShield extends AbstractFriendCard {
    public static final String ID = "nearlmod:ArtsShield";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/artsshield.png";
    private static final int COST = 1;
    private static final int BLOCK_AMT = 5;
    private static final int TEMP_HP = 5;
    private static final int UPGRADE_PLUS_BLOCK = 2;
    private static final int UPGRADE_PLUS_HP = 2;
    public static final String BG_IMG = "images/512/bg_friend_test.png";

    public ArtsShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Nightingale");
        magicNumber = baseMagicNumber = BLOCK_AMT;
        secondMagicNumber = baseSecondMagicNumber = TEMP_HP;
        updateDmg();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(p, p, secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ArtsShield();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
            upgradeSecondMagicNumber(UPGRADE_PLUS_HP);
        }
    }
}