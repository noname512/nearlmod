package nearlmod.cards.friendcards;

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
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.Viviana;
import nearlmod.powers.GlimmeringTouchPower;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

import java.util.Iterator;

public class GlimmeringTouch extends AbstractNearlCard {
    public static final String ID = "nearlmod:GlimmeringTouch";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/glimmeringtouch.png";
    private static final int COST = 2;
    private static final int CHARGE_TURN = 2;
    private static final int UPGRADE_CHARGE_TURN = 1;
    private static final int ATTACK_DMG = 30;

    public GlimmeringTouch() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION + " NL 虚无 。 NL 消耗 。",
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.SELF);
        tags.add(NearlTags.IS_FRIEND_CARD);
        isMultiDamage = true;
        magicNumber = baseMagicNumber = ATTACK_DMG;
        secondMagicNumber = CHARGE_TURN;
        belongFriend = "nearlmod:Viviana";
        exhaust = true;
        isEthereal = true;
        updateDmg();
    }

    public void updateDmg() {
        AbstractPlayer p = AbstractDungeon.player;
        if (p == null || p.orbs == null) return;
        for (Iterator it = p.orbs.iterator(); it.hasNext();) {
            AbstractOrb orb = (AbstractOrb)it.next();
            if (orb instanceof Viviana) {
                int str = ((Viviana)orb).passiveAmount;
                magicNumber += str;
            }
        }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator it = AbstractDungeon.player.orbs.iterator();
        while (it.hasNext()) {
            AbstractOrb orb = (AbstractOrb)it.next();
            if (orb instanceof Viviana) {
                ((Viviana)orb).startCharging(secondMagicNumber);
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GlimmeringTouchPower(p, secondMagicNumber, magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GlimmeringTouch();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            secondMagicNumber = UPGRADE_CHARGE_TURN;
        }
    }

    @Override
    public void applyFriendPower(int amount) {
        magicNumber += amount;
        isMagicNumberModified = true;
    }
}
