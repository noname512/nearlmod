package nearlmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.special.LightCard;
import nearlmod.characters.Nearl;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.stances.AtkStance;

public class Unsheathed extends AbstractNearlCard {
    public static final String ID = "nearlmod:Unsheathed";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/unsheathed.png";
    private static final int COST = 2;
    private static final int ATTACK_DMG = 15;
    private static final int UPGRADE_PLUS_DMG = 5;

    public Unsheathed() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        cardsToPreview = new LightCard();
        previewList = Nearl.getLightCards();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            addToBot(new ChangeStanceAction(new AtkStance()));
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        addToBot(new MakeTempCardInHandAction(new LightCard()));
    }

    private void preUpd() {
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            baseDamage += AtkStance.incNum;
            baseDamage += AtkStance.atkInc;
        }
    }

    private void postUpd() {
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            baseDamage -= AtkStance.incNum;
            baseDamage -= AtkStance.atkInc;
            isDamageModified = (baseDamage != damage);
        }
    }

    @Override
    public void applyPowers() {
        preUpd();
        super.applyPowers();
        postUpd();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        preUpd();
        super.calculateCardDamage(mo);
        postUpd();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Unsheathed();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
