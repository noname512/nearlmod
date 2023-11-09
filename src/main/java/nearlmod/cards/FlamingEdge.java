package nearlmod.cards;

import basemod.helpers.BaseModCardTags;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.colorless.DarkShackles;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.FlamingEdgePower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class FlamingEdge extends AbstractNearlCard {
    public static final String ID = "nearlmod:FlamingEdge";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flamingedge.png";
    private static final int COST = 1;
    private static final int LIGHT_GAIN = 4;
    private static final int UPGRADE_LIGHT_GAIN = 2;

    public FlamingEdge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);

        magicNumber = baseMagicNumber = LIGHT_GAIN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FlamingEdgePower(p, magicNumber)));
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlamingEdge();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_LIGHT_GAIN);
        }
    }
}
