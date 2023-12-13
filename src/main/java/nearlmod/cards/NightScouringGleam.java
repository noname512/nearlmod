package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.powers.MyFadingPower;

public class NightScouringGleam extends AbstractNearlCard {
    public static final String ID = "nearlmod:NightScouringGleam";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nightscouringgleam.png";
    private static final int COST = 1;
    private static final int GET_STRENGTH = 15;
    private static final int UPGRADE_EXTRA_BUFFER = 1;
    private static final int GET_BUFFER = 3;
    private static final int DIE_TURN = 2;

    public NightScouringGleam() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                AbstractCard.CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
        baseMagicNumber = magicNumber = GET_BUFFER;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, GET_STRENGTH), GET_STRENGTH));
        addToBot(new ApplyPowerAction(p, p, new BufferPower(p, magicNumber), magicNumber));
        addToBot(new ApplyPowerAction(p,p, new MyFadingPower(p, DIE_TURN), DIE_TURN));
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NightScouringGleam();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
            upgradeMagicNumber(UPGRADE_EXTRA_BUFFER);
            selfRetain = true;
        }
    }
}
