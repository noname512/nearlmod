package nearlmod.cards.friendcards;

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
import nearlmod.orbs.Fartooth;
import nearlmod.patches.AbstractCardEnum;

public class WhipSword extends AbstractFriendCard {
    public static final String ID = "nearlmod:WhipSword";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/lsswiftsword.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 3;
    private static final int EXTRA_DMG = 8;

    public WhipSword() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.SPECIAL, CardTarget.ALL_ENEMY, "nearlmod:Whislash");
        magicNumber = baseMagicNumber = EXTRA_DMG;
        secondMagicNumber = baseSecondMagicNumber = ATTACK_DMG;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int dmg = secondMagicNumber;
        if (p.hasPower("Strength"))
            dmg += p.getPower("Strength").amount;
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            addToBot(new DamageAction(ms, new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL)));
        for (AbstractOrb orb : p.orbs)
            if (orb instanceof AbstractFriend) {
                dmg = secondMagicNumber + ((AbstractFriend)orb).trustAmount;
                if (orb instanceof Fartooth)
                    for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
                        addToBot(new DamageAction(ms, new DamageInfo(null, dmg, DamageInfo.DamageType.THORNS)));
                else
                    for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
                        addToBot(new DamageAction(ms, new DamageInfo(p, dmg, DamageInfo.DamageType.NORMAL)));
            }
        if (upgraded)
            for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
                addToBot(new DamageAction(ms, new DamageInfo(p, magicNumber, DamageInfo.DamageType.NORMAL)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new WhipSword();
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
