package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.orbs.Blemishine;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LightPower;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

import java.util.Iterator;

public class SwordShield extends AbstractNearlCard {
    public static final String ID = "nearlmod:SwordShield";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/swordshield.png";
    private static final int COST = 1;
    private static final int LIGHT_ADD = 6;
    private static final int UPGRADE_PLUS_LIGHT = 2;
    private static final int EXTRA_INC = 1;
    private static final int UPGRADE_PLUS_EXTRA = 1;

    public SwordShield() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = LIGHT_ADD;
        secondMagicNumber = baseSecondMagicNumber = EXTRA_INC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasBlemishine = false;
        for (Iterator<AbstractOrb> it = p.orbs.iterator(); it.hasNext(); ) {
            AbstractOrb orb = it.next();
            if (orb instanceof Blemishine) {
                hasBlemishine = true;
                break;
            }
        }
        if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new DefStance()));
            if (hasBlemishine) DefStance.defInc += secondMagicNumber;
        } else {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
            if (hasBlemishine) AtkStance.atkInc += secondMagicNumber;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LightPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SwordShield();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
            upgradeSecondMagicNumber(UPGRADE_PLUS_EXTRA);
        }
    }
}
