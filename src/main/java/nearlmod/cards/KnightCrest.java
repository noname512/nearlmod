package nearlmod.cards;

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
import nearlmod.actions.UseLightAction;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.stances.AtkStance;
import nearlmod.stances.DefStance;

public class KnightCrest extends AbstractNearlCard {
    public static final String ID = "nearlmod:KnightCrest";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/knightcrest.png";
    private static final int COST = 0;
    private static final int ATTACK_DMG = 3;
    private static final int UPGRADE_PLUS_DMG = 2;

    public KnightCrest() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.ENEMY);
        damage = baseDamage = ATTACK_DMG;
        tags.add(NearlTags.IS_USE_LIGHT_AFTER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.stance.ID.equals(AtkStance.STANCE_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ChangeStanceAction(new AtkStance()));
        }
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, this.damageType), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new UseLightAction(m));
    }

    @Override
    public void applyPowers() {
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            this.baseDamage += AtkStance.incNum;
            this.baseDamage += AtkStance.atkInc;
        }
        super.applyPowers();
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            this.baseDamage -= AtkStance.incNum;
            this.baseDamage -= AtkStance.atkInc;
            isDamageModified = (baseDamage != damage);
        }
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            this.baseDamage += AtkStance.incNum;
            this.baseDamage += AtkStance.atkInc;
        }
        super.calculateCardDamage(mo);
        if (!AbstractDungeon.player.stance.ID.equals(AtkStance.STANCE_ID)) {
            this.baseDamage -= AtkStance.incNum;
            this.baseDamage -= AtkStance.atkInc;
            isDamageModified = (baseDamage != damage);
        }
    }
    @Override
    public AbstractCard makeCopy() {
        return new KnightCrest();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
