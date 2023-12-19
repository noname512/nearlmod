package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Whislash;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.stances.AtkStance;

import java.util.ArrayList;

public class DEPRECATEDReservedForce extends AbstractNearlCard {
    public static final String ID = "nearlmod:DEPRECATEDReservedForce";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/glowingbluebird.png";
    private static final int COST = 1;
    private static final int ATTACK_DMG = 5;
    private static final int UPGRADE_PLUS_DMG = 3;

    public DEPRECATEDReservedForce() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Whislash.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));
        if (extraTriggered()) {
            ArrayList<AbstractFriendCard> list = new ArrayList<>();
            if (p.stance.ID.equals(AtkStance.STANCE_ID)) {
                for (AbstractCard c : Nearl.friendCards.group)
                    if (!c.hasTag(NearlTags.IS_UNIQUE_CARD) && c.type == CardType.ATTACK)
                        list.add((AbstractFriendCard)c.makeCopy());
            } else {
                for (AbstractCard c : Nearl.friendCards.group)
                    if (!c.hasTag(NearlTags.IS_UNIQUE_CARD) && c.type == CardType.SKILL)
                        list.add((AbstractFriendCard)c.makeCopy());
            }
            addToBot(new AddFriendCardToHandAction(list.get(AbstractDungeon.cardRng.random(0, list.size() - 1))));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new DEPRECATEDReservedForce();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
