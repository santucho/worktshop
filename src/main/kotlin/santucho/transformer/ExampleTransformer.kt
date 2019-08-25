package santucho.transformer

import com.google.inject.Singleton
import santucho.client.dto.example.ExampleDTO
import santucho.model.ExampleModel

@Singleton
class ExampleTransformer {

    /**
     * We can pass unordered parameters to a constructor if we explicit the parameter name.
     * We can ignore parameters that have a default value
     * */
    fun transform(exampleDTO: ExampleDTO): ExampleModel {
        return ExampleModel(
            id = exampleDTO.id,
            value =exampleDTO.value
        )
    }

}
