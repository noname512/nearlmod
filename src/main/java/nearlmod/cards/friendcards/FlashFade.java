package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nearlmod.actions.UseShadowAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.ExsanguinationPower;
import nearlmod.stances.AtkStance;

import static java.lang.Integer.min;

public class FlashFade extends AbstractFriendCard {
    public static final String ID = "nearlmod:FlashFade";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/flashfade.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 8;
    private static final int ATTACK_TIMES = 2;
    private static final int UPGRADE_PLUS_TIMES = 1;

    public FlashFade() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.FRIEND_BLUE,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Viviana");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_TIMES;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        if (p.getPower("nearlmod:Shadow") != null) {
            magicNumber += p.getPower("nearlmod:Shadow").amount;
        }
        AbstractPower str = p.getPower("Strength");
        if (str != null) {
            magicNumber += str.amount;
        }
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            magicNumber += calc();
        }
        if (magicNumber < 0) {
            magicNumber = 0;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    int calc() {
        int num =  AtkStance.incNum + AtkStance.atkInc;
        if (AbstractDungeon.player.hasPower(ExsanguinationPower.POWER_ID)) {
            int strength = 0;
            if (AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
                strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount;
            }
            num = min(num, -strength);
        }
        return num;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
        DamageInfo info = new DamageInfo(p, magicNumber);
        info.name = belongFriend + AbstractFriendCard.damageSuffix;
        for (int i = 1; i <= secondMagicNumber; i++)
            addToBot(new DamageAction(m, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new UseShadowAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlashFade();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (!super.canUse(p, m)) return false;
        if (p.hasPower("nearlmod:GlimmeringTouchPower")) {
            this.cantUseMessage = CardCrawlGame.languagePack.getUIString("nearlmod:Can'tUseMessage").TEXT[0];
            return false;
        }
        return true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_TIMES);
        }
    }
}
