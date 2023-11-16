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
import nearlmod.actions.UseShadowAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

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
    private int extraStr;

    public FlashFade() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Viviana");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_TIMES;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        if (p.getPower("nearlmod:Shadow") != null)
        {
            magicNumber += p.getPower("nearlmod:Shadow").amount;
        }
        extraStr = 0;
        AbstractPower str = p.getPower("Strength");
        if (str != null) {
            magicNumber += str.amount;
            extraStr = str.amount;
        }
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            magicNumber += AtkStance.atkInc + AtkStance.incNum;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
        for (int i = 1; i <= secondMagicNumber; i++)
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new UseShadowAction(p));
    }

    @Override
    public AbstractCard makeCopy() {
        return new FlashFade();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeSecondMagicNumber(UPGRADE_PLUS_TIMES);
        }
    }
}
