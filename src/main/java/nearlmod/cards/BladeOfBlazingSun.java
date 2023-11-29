package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.SummonOrbAction;
import nearlmod.orbs.Blemishine;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.BladeOfBlazingSunPower;
import nearlmod.relics.LateLight;

public class BladeOfBlazingSun extends AbstractNearlCard {
    public static final String ID = "nearlmod:BladeOfBlazingSun";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/gloriouskazimierz.png";
    private static final int COST = 1;

    public BladeOfBlazingSun() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Blemishine.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (extraTriggered()) {
            addToBot(new ApplyPowerAction(p, p, new BladeOfBlazingSunPower(p)));
        } else {
            if (!p.hasRelic(LateLight.ID))
                addToBot(new SummonOrbAction(new Blemishine()));
            else
                p.getRelic(LateLight.ID).flash();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BladeOfBlazingSun();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
