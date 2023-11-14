package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;

import java.util.ArrayList;

public class FlameShadow extends AbstractFriendCard {
    public static final String ID = "nearlmod:FlameShadow";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flameshadow.png";
    public static final String BG_IMG = "images/512/bg_friend_test.png";
    private static final int COST = 1;
    private static final int LIGHT_ADD = 10;

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
        if (power != null) {
            dmg += power.amount * 2;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, power));
        }
        if (upgraded) dmg += LIGHT_ADD * 2;
        dmg += magicNumber;
//        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, dmg, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.LIGHTNING));
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
        for (AbstractMonster ms : monsters) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(ms, new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.LIGHTNING));
        }
        Viviana.uniqueUsed = true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlameShadow();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
