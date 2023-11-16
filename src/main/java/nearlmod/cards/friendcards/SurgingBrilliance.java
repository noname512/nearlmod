package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.orbs.AbstractFriend;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.LightPower;

public class SurgingBrilliance extends AbstractFriendCard {
    public static final String ID = "nearlmod:SurgingBrilliance";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/surgingbrilliance.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 5;
    private static final int LIGHT_INC = 7;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int UPGRADE_PLUS_LIGHT = 2;

    public SurgingBrilliance() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ENEMY, "nearlmod:Blemishine");
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = LIGHT_INC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new LightPower(p, secondMagicNumber), secondMagicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SurgingBrilliance();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG);
            upgradeSecondMagicNumber(UPGRADE_PLUS_LIGHT);
        }
    }

    @Override
    public void applyFriendPower() {
        magicNumber = baseMagicNumber;
        secondMagicNumber = baseSecondMagicNumber;
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        for (AbstractOrb orb : p.orbs) {
            if (orb instanceof AbstractFriend)
                if (orb.ID.equals(belongFriend)) {
                    magicNumber += orb.passiveAmount;
                    secondMagicNumber += orb.passiveAmount;
                }
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
        isSecondMagicNumberModified = (secondMagicNumber != baseSecondMagicNumber);
    }
}
