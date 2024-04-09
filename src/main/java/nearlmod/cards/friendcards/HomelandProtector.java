package nearlmod.cards.friendcards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.AllySupportPower;
import nearlmod.powers.CostNextTurnPower;
import nearlmod.powers.LossEnergyNextTurnPower;

import static nearlmod.patches.NearlTags.IS_KNIGHT_CARD;

public class HomelandProtector extends AbstractFriendCard {
    public static final String ID = "nearlmod:HomelandProtector";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/nearldefend.png";
    private static final int COST = 0;
    private static final int BLOCK_GAIN = 10;
    private static final int UPGRADE_PLUS_BLOCK = 4;
    private static final int LOSS_COST = 1;

    public HomelandProtector() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Aurora");
        magicNumber = baseMagicNumber = BLOCK_GAIN;
        secondMagicNumber = baseSecondMagicNumber = LOSS_COST;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, magicNumber));
        addToBot(new ApplyPowerAction(p, p, new LossEnergyNextTurnPower(p, secondMagicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new HomelandProtector();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
        }
    }
}
