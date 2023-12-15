package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.StormCounterAttackPower;

public class StormCounterAttack extends AbstractNearlCard {
    public static final String ID = "nearlmod:StormCounterAttack";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/stormcounterattack.png";
    private static final int COST = 0;
    private static final int ATTACK_MUL = 2;
    private static final int UPGRADE_PLUS_MUL = 1;

    public StormCounterAttack() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = ATTACK_MUL;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StormCounterAttackPower(p, magicNumber)));
        addToBot(new PressEndTurnButtonAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new StormCounterAttack();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MUL);
        }
    }
}
