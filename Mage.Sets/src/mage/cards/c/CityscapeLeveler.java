package mage.cards.c;

import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.CreateTokenControllerTargetEffect;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.abilities.keyword.TrampleAbility;
import mage.abilities.keyword.UnearthAbility;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.GameEvent;
import mage.game.permanent.token.PowerstoneToken;
import mage.target.common.TargetNonlandPermanent;

import java.util.UUID;

/**
 * @author weirddan455
 */
public final class CityscapeLeveler extends CardImpl {

    public CityscapeLeveler(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId, setInfo, new CardType[]{CardType.ARTIFACT, CardType.CREATURE}, "{8}");

        this.subtype.add(SubType.CONSTRUCT);
        this.power = new MageInt(8);
        this.toughness = new MageInt(8);

        // Trample
        this.addAbility(TrampleAbility.getInstance());

        // When you cast this spell and whenever Cityscape Leveler attacks, destroy up to one target nonland permanent. Its controller creates a tapped Powerstone token.
        Ability ability = new CityscapeLevelerAbility();
        ability.addEffect(new CreateTokenControllerTargetEffect(new PowerstoneToken(), 1, true));
        ability.addTarget(new TargetNonlandPermanent(0, 1));
        this.addAbility(ability);

        // Unearth {8}
        this.addAbility(new UnearthAbility(new ManaCostsImpl<>("{8}")));
    }

    private CityscapeLeveler(final CityscapeLeveler card) {
        super(card);
    }

    @Override
    public CityscapeLeveler copy() {
        return new CityscapeLeveler(this);
    }
}

class CityscapeLevelerAbility extends TriggeredAbilityImpl {

    public CityscapeLevelerAbility() {
        super(Zone.ALL, new DestroyTargetEffect());
        setTriggerPhrase("When you cast this spell and whenever {this} attacks, ");
    }

    private CityscapeLevelerAbility(final CityscapeLevelerAbility ability) {
        super(ability);
    }

    @Override
    public CityscapeLevelerAbility copy() {
        return new CityscapeLevelerAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        switch (event.getType()) {
            case SPELL_CAST:
            case DECLARED_ATTACKERS:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (sourceId == null) {
            return false;
        }
        switch (event.getType()) {
            case SPELL_CAST:
                return sourceId.equals(event.getSourceId());
            case DECLARED_ATTACKERS:
                return game.getCombat().getAttackers().contains(sourceId);
            default:
                return false;
        }
    }
}