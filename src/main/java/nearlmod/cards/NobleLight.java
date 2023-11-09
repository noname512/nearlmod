package nearlmod.cards;

import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

import java.util.Iterator;

public class NobleLight extends AbstractNearlCard {
    public static final String ID = "nearlmod:NobleLight";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/noblelight.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public NobleLight() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator it = p.orbs.iterator();
        while (it.hasNext()) {
            AbstractOrb orb = (AbstractOrb) it.next();
            if (orb instanceof Viviana)
                ((Viviana)orb).applyStrength(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new NobleLight();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
