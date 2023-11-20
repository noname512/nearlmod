package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.patches.AbstractCardEnum;

public class SweepWrong extends AbstractNearlCard {
    public static final String ID = "nearlmod:SweepWrong";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 12;
    private static final int EXTRA_DAMAGE = 5;
    private static final int UPGRADE_DMG_INC = 2;
    private static final int UPGRADE_EXTRA_DMG_INC = 2;

    public SweepWrong() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = EXTRA_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster mo : AbstractDungeon.getMonsters().monsters) {
            if (!mo.isDeadOrEscaped()) {
                if (mo.getIntentBaseDmg() >= 0) {
                    addToBot(new DamageAction(mo, new DamageInfo(p, damage + magicNumber, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                } else {
                    addToBot(new DamageAction(mo, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
            }
        }
    }

    public void applyPowers() {
        this.baseDamage -= 7;
        this.baseMagicNumber = this.baseDamage;
        super.applyPowers();
        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;
        this.baseDamage += 7;
        super.applyPowers();
    }

    @Override
    public AbstractCard makeCopy() {
        return new SweepWrong();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DMG_INC);
            upgradeMagicNumber(UPGRADE_EXTRA_DMG_INC);
        }
    }
}
