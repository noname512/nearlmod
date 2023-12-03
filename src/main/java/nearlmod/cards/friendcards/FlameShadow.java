package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.actions.UseShadowAction;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.PoemsLooksPower;
import nearlmod.powers.ShadowPower;

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
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.SELF, "nearlmod:Viviana");
        magicNumber = baseMagicNumber = 0;
        bannerSmallRegion = ImageMaster.CARD_BANNER_RARE;
        bannerLargeRegion = ImageMaster.CARD_BANNER_RARE_L;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower power = p.getPower("nearlmod:LightPower");
        int dmg = baseMagicNumber;
        int light = 0;
        if (power != null) {
            dmg += power.amount * 2;
            light += power.amount;
            addToBot(new RemoveSpecificPowerAction(p, p, power));
        }
        addToBot(new PureDamageAllEnemiesAction(p, dmg, belongFriend + damageSuffix));
        Viviana.uniqueUsed = true;
        addToBot(new UseShadowAction(p));
        if (light != 0) {
            addToBot(new ApplyPowerAction(p, p, new ShadowPower(p, light)));
        }
        addToBot(new ApplyPowerAction(p, p, new PoemsLooksPower(p)));
        applyPowers();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (p.hasPower("nearlmod:GlimmeringTouchPower")) {
            this.cantUseMessage = "薇薇安娜正在蓄力中";
            return false;
        }
        return true;
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
