package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.SpotlightPlayerEffect;
import nearlmod.cards.AbstractNearlCard;

public class MedalOfChampion extends AbstractNearlCard {
    public static final String ID = "nearlmod:MedalOfChampion";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/medalofchampion.png";
    private static final int COST = 0;
    private static final int GOLD_AMT = 25;
    private static final int UPGRADE_PLUS_GOLD = 5;

    public MedalOfChampion() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        magicNumber = baseMagicNumber = GOLD_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.effectList.add(new RainingGoldEffect(this.magicNumber * 2, true));
        AbstractDungeon.effectsQueue.add(new SpotlightPlayerEffect());
        addToBot(new GainGoldAction(magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MedalOfChampion();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_GOLD);
        }
    }
}
