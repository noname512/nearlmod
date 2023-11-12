package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.WeakPower;
import nearlmod.actions.SummonOrbAction;
import nearlmod.actions.WeakenAllAction;
import nearlmod.orbs.Shining;
import nearlmod.patches.AbstractCardEnum;

import java.util.ArrayList;

public class DawnDuskSaber extends AbstractNearlCard {
    public static final String ID = "nearlmod:DawnDuskSaber";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/dawndusksaber.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int ATTACK_DMG = 3;

    public DawnDuskSaber() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean hasOrb = false;
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof Shining) {
                ((Shining) orb).upgrade();
                hasOrb = true;
            }
        if (!hasOrb)
            addToBot(new SummonOrbAction(new Shining()));
        addToBot(new DamageAllEnemiesAction(p, DamageInfo.createDamageMatrix(damage, true, false), DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        if (upgraded) addToBot(new WeakenAllAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DawnDuskSaber();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
