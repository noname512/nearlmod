package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.WeakenAllAction;
import nearlmod.orbs.Shining;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.powers.CreedFieldPower;

public class Oaths extends AbstractNearlCard {
    public static final String ID = "nearlmod:Oaths";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/nearlstrike.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 10;
    private static final int WEAKNESS_AMT = 1;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int UPGRADE_PLUS_WEAKNESS = 1;

    public Oaths() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        damage = baseDamage = ATTACK_DMG;
        magicNumber = baseMagicNumber = WEAKNESS_AMT;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Shining.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, damage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new WeakenAllAction(p, magicNumber));
        if (extraTriggered())
            addToBot(new ApplyPowerAction(p, p, new CreedFieldPower(p, 1)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Oaths();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_WEAKNESS);
        }
    }
}
