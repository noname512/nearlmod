package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ClosedHopePower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class ClosedHope extends AbstractFriendCard {
    public static final String ID = "nearlmod:ClosedHope";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/closedhope.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 8;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    private static final int ARTIFACT_NUM = 2;

    public ClosedHope() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Nightingale");
        magicNumber = baseMagicNumber = BLOCK_AMT;
        secondMagicNumber = baseSecondMagicNumber = ARTIFACT_NUM;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.stance.ID.equals(DefStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new DefStance()));
        }
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ClosedHopePower(p, secondMagicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ClosedHope();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BLOCK);
        }
    }
}
