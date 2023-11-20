package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.actions.UseShadowAction;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.PoemsLooksPower;
import nearlmod.powers.ShadowPower;

import java.util.ArrayList;

public class FlameShadow extends AbstractFriendCard {
    public static final String ID = "nearlmod:FlameShadow";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flameshadow.png";
    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;

    public FlameShadow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Viviana");
        magicNumber = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower power = p.getPower("nearlmod:LightPower");
        int dmg = 0;
        int light = 0;
        if (power != null) {
            dmg += power.amount * 2;
            light += power.amount;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, power));
        }
        dmg += magicNumber;
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for (AbstractMonster ms : monsters) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(ms, new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.LIGHTNING));
        }
        Viviana.uniqueUsed = true;
        AbstractDungeon.actionManager.addToBottom(new UseShadowAction(p));
        if (light != 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadowPower(p, light)));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PoemsLooksPower(p)));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p.getPower("nearlmod:Shadow") != null) {
            magicNumber += p.getPower("nearlmod:Shadow").amount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameShadow();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
