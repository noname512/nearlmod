package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.actions.WeakenAllAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.characters.Nearl;
import nearlmod.orbs.Shining;
import nearlmod.orbs.Viviana;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.BFPPower;

public class DawnDuskSaber extends AbstractNearlCard {
    public static final String ID = "nearlmod:DawnDuskSaber";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/dawndusksaber.png";
    private static final int COST = 2;
    private static final int UPGRADE_COST = 1;
    private static final int ATTACK_DMG = 4;

    public DawnDuskSaber() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = ATTACK_DMG;
        tags.add(NearlTags.IS_SUMMON_CARD);

        previewList = Nearl.getFriendCard(Shining.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        addToBot(new SummonFriendAction(new Shining()));
        addToBot(new PureDamageAllEnemiesAction(p, magicNumber, Shining.ORB_ID + AbstractFriendCard.damageSuffix, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (upgraded) addToBot(new WeakenAllAction(p));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        magicNumber = baseMagicNumber;
        for (AbstractOrb orb : AbstractDungeon.player.orbs)
            if (orb instanceof Shining) {
                magicNumber += ((Shining)orb).getTrustAmount();
                magicNumber += 2;
                break;
            }
        isMagicNumberModified = (magicNumber != baseMagicNumber);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        applyPowers();
        int ms_count = 0;
        for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDeadOrEscaped()) {
                ms_count ++;
            }
        }
        if (ms_count == 1) {
            for (AbstractMonster m:AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    magicNumber = calculateSingleDamage(m, magicNumber, true);
                    isMagicNumberModified = (magicNumber != baseMagicNumber);
                }
            }
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new DawnDuskSaber();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
