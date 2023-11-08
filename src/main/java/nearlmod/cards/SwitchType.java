package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.characters.Nearl;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.*;

public class SwitchType extends AbstractNearlCard {
    public static final String ID = "nearlmod:SwitchType";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/majestylight.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public SwitchType() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.BASIC, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new DefStance()));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwitchType();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
