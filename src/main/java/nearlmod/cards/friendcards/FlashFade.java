package nearlmod.cards.friendcards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

import java.util.Iterator;

public class FlashFade extends AbstractNearlCard {
    public static final String ID = "nearlmod:FlashFade";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 8;
    private static final int ATTACK_TIMES = 2;
    private static final int UPGRADE_PLUS_TIMES = 1;
    private int extraStr;

    public FlashFade() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION + " NL 虚无 。 NL 消耗 。",
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ENEMY);
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_TIMES;
        tags.add(NearlTags.IS_FRIEND_CARD);
        belongFriend = "nearlmod:Viviana";
        exhaust = true;
        isEthereal = true;
        extraStr = 0;
        updateDmg();
    }

    public void updateDmg() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        for (Iterator it = p.orbs.iterator(); it.hasNext();) {
            AbstractOrb orb = (AbstractOrb)it.next();
            if (orb instanceof Viviana)
                if (((Viviana)orb).ORB_ID.equals(belongFriend)) {
                    int str = ((Viviana)orb).passiveAmount;
                    magicNumber += str;
                }
        }
        AbstractPower str = p.getPower("Strength");
        if (str != null) {
            magicNumber += str.amount;
            extraStr = str.amount;
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 1; i <= secondMagicNumber; i++)
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
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

    @Override
    public void applyFriendPower(int amount) {
        magicNumber += amount;
        isMagicNumberModified = true;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        AbstractPower str = AbstractDungeon.player.getPower("Strength");
        if (str != null) {
            magicNumber += str.amount - extraStr;
            extraStr = str.amount;
        }
        isMagicNumberModified = true;
    }
}
