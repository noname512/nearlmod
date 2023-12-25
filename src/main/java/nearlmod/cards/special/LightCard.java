package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.SkillFromDeckToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.AllFromDeckToHandAction;
import nearlmod.actions.UseLightAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.cards.SwitchType;
import nearlmod.powers.LightPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class LightCard extends AbstractNearlCard {
    public static final String ID = "nearlmod:Radiance";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH_PREFIX = "images/cards/lightcard/lightcard_";
    public static final String IMG_PATH_POSTFIX = ".png";
    private static final int COST = 0;
    private static final int EXTRA_LIGHT = 4;
    private static final int UPGRADE_PLUS_LIGHT = 2;
    private static final int STR_LOSS = 2;
    private static final int UPGRADE_PLUS_LOSS = 1;
    private int type;

    public LightCard() {
        this(0);
    }

    public LightCard(int type) {
        super(ID, NAME, IMG_PATH_PREFIX + type + IMG_PATH_POSTFIX, COST, EXTENDED_DESCRIPTION[0] + DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        exhaust = true;
        this.type = type;
    }

    @Override
    public boolean extraTriggered() {
        if (type == 2) return AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID);
        else if (type == 3) return AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID);
        else return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (type == 1 || ((type == 4 || type == 5 || type == 6) && upgraded))
            addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber)));
        addToBot(new UseLightAction(p, m));
        switch (type) {
            case 2:
                if (extraTriggered()) {
                    addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber)));
                    if (!m.hasPower("Artifact")) {
                        addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, magicNumber)));
                    }
                }
                break;
            case 3:
                if (extraTriggered()) {
                    if (!upgraded)
                        addToBot(new SkillFromDeckToHandAction(1));
                    else
                        addToBot(new AllFromDeckToHandAction(1));
                }
                break;
            case 6:
                addToBot(new MakeTempCardInHandAction(cardsToPreview));
                break;
        }
    }

    @Override
    public void applyPowers() {
        switchedStance();
        super.applyPowers();
    }

    @Override
    public void switchedStance() {
        if (AbstractDungeon.player.stance.ID.equals(DefStance.STANCE_ID)) this.target = CardTarget.SELF;
        else this.target = CardTarget.ENEMY;
    }

    @Override
    public void triggerWhenCopied() {
        if (type == 0) {
            type = AbstractDungeon.cardRng.random(1, 6);
            rawDescription = EXTENDED_DESCRIPTION[type] + DESCRIPTION;
            textureImg = IMG_PATH_PREFIX + type + IMG_PATH_POSTFIX;
            loadCardImage(textureImg);
            switch (type) {
                case 1:
                    magicNumber = baseMagicNumber = EXTRA_LIGHT;
                    break;
                case 2:
                    magicNumber = baseMagicNumber = STR_LOSS;
                    break;
                case 4:
                    selfRetain = true;
                    magicNumber = baseMagicNumber = 0;
                    break;
                case 5:
                    magicNumber = baseMagicNumber = 0;
                    AbstractCard card = new SwitchType();
                    card.upgrade();
                    card.exhaust = true;
                    card.isEthereal = true;
                    addToBot(new MakeTempCardInHandAction(card, false));
                    break;
                case 6:
                    magicNumber = baseMagicNumber = 0;
                    cardsToPreview = new LightCard();
                    cardsToPreview.upgrade();
                    break;
            }
            initializeDescription();
            if (upgraded) {
                upgraded = false;
                name = NAME;
                upgrade();
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new LightCard(type);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            switch (type) {
                case 1:
                    upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
                    break;
                case 2:
                    upgradeMagicNumber(UPGRADE_PLUS_LOSS);
                    break;
                case 3:
                    rawDescription = EXTENDED_DESCRIPTION[7] + DESCRIPTION;
                    initializeDescription();
                    break;
                case 4:
                case 5:
                case 6:
                    upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
                    rawDescription = EXTENDED_DESCRIPTION[8] + EXTENDED_DESCRIPTION[type] + DESCRIPTION;
                    initializeDescription();
            }
        }
    }
}
