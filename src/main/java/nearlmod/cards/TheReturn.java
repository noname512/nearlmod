package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.green.Backstab;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.SummonOrbAction;
import nearlmod.orbs.Nightingale;
import nearlmod.orbs.Shining;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ExsanguinationPower;
import nearlmod.powers.InvinciblePower;
import nearlmod.stances.AtkStance;

public class TheReturn extends AbstractNearlCard {
    public static final String ID = "nearlmod:TheReturn";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/waytochampion.png";
    private static final int COST = 3;

    public TheReturn() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean isShiningExsit = false;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof Shining) {
                ((Shining) orb).upgrade();
                isShiningExsit = true;
                break;
            }
        if (!isShiningExsit) {
            AbstractDungeon.actionManager.addToBottom(new SummonOrbAction(new Shining()));
        }
        boolean isNightangleExsit = false;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof Nightingale) {
                ((Nightingale) orb).upgrade();
                isNightangleExsit = true;
                break;
            }
        if (!isNightangleExsit) {
            AbstractDungeon.actionManager.addToBottom(new SummonOrbAction(new Nightingale()));
        }

        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new TheReturn();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION + DESCRIPTION;
            isInnate = true;
            initializeDescription();
        }
    }
}
