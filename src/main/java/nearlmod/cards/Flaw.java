package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LoseVulnerablePower;

public class Flaw extends AbstractNearlCard {
    public static final String ID = "nearlmod:Flaw";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/flaw.png";
    private static final int COST = 1;
    private static final int VUL_AMT = 2;
    private static final int UPGRADED_PLUS_VUL = 1;

    public Flaw() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = VUL_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, magicNumber, false)));
        addToBot(new ApplyPowerAction(p, p, new LoseVulnerablePower(p, magicNumber)));
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (!ms.isDeadOrEscaped())
                addToBot(new ApplyPowerAction(ms, p, new VulnerablePower(ms, magicNumber, false)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Flaw();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_PLUS_VUL);
        }
    }
}
