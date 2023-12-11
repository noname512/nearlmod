package nearlmod.cards.special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.UseLightAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.powers.LightPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class NearlCard extends AbstractNearlCard {
    public static final String ID = "nearlmod:NearlCard";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlcard.png";
    private static final int COST = 0;
    private static final int LIGHT_AMT = 10;
    private static final int UPGRADE_PLUS_LIGHT = 2;

    public NearlCard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        magicNumber = baseMagicNumber = LIGHT_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) addToBot(new ChangeStanceAction(new DefStance()));
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber)));
        addToBot(new UseLightAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new NearlCard();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
        }
    }
}
